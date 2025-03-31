package com.example.pubsub.service;

import com.example.pubsub.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PubSubMensageria {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.subscription-id}")
    private String subscriptionId;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Subscriber subscriber;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void iniciarListener() {
        System.out.println("🚀 Iniciando o consumidor de mensagens Pub/Sub...");
        iniciarAssinatura(projectId, subscriptionId);
    }

    public void iniciarAssinatura(String projectId, String subscriptionId) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            try {
                System.out.println("📩 Mensagem recebida!");
                System.out.println("🔹 ID: " + message.getMessageId());
                String content = message.getData().toStringUtf8();
                System.out.println("🔹 Conteúdo: " + content);


                // Processar a mensagem
                processarMensagem(content);
//                consumer.ack();
                System.out.println("✅ Mensagem processada com sucesso!");
            } catch (Exception e) {
                System.err.println("❌ Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
//                consumer.nack();
            }
        };

        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();

            subscriber.addListener(new Subscriber.Listener() {
                @Override
                public void failed(Subscriber.State from, Throwable failure) {
                    System.err.println("❌ Subscriber falhou: " + failure.getMessage());
                    failure.printStackTrace();
                }
            }, MoreExecutors.directExecutor());

            subscriber.startAsync().awaitRunning();
            System.out.println("📡 Escutando mensagens de " + subscriptionName + "...");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (subscriber != null) {
                    subscriber.stopAsync();
                    System.out.println("🛑 Subscriber encerrado.");
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao iniciar o subscriber: " + e.getMessage());
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }

    private void processarMensagem(String content) throws Exception {
        PubSubMessage messageData = objectMapper.readValue(content, PubSubMessage.class);

        String transactionId = UUID.randomUUID().toString();

        // Salvar cliente
        Client client = new Client();
        client.setExternalId(messageData.getCustomer().getId());
        client.setName(messageData.getCustomer().getName());
        clientService.save(client);

        // Salvar mensagem
        Msg msg = new Msg();
        msg.setUuid(messageData.getUuid());
        msg.setCreatedAt(LocalDateTime.parse(messageData.getCreatedAt(), DATE_FORMATTER));
        msg.setType(messageData.getType());
        msg.setClientId(client.getExternalId()); // Usar externalId do client salvo
        msg.setTransactionId(transactionId);
        msgService.save(msg);

        // Salvar reservas
        for (PubSubMessage.Room room : messageData.getRooms()) {
            Reservation reservation = new Reservation();
            reservation.setClientId(client.getExternalId());
            reservation.setTransactionId(transactionId);
            reservation.setRoomId(room.getId());
            reservation.setDailyRate(room.getDailyRate());
            reservation.setNumberOfDays(room.getNumberOfDays());
            reservation.setReservationDate(room.getReservationDate());

            // Tratar subcategory opcional
            String subCategoryId = room.getCategory().getSubCategory() != null ?
                    room.getCategory().getSubCategory().getId() :
                    "";

            reservation.setCategoryId(room.getCategory().getId());
            reservation.setSubCategoryId(subCategoryId);

            reservationService.save(reservation);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (subscriber != null) {
            subscriber.stopAsync();
            System.out.println("🛑 Subscriber sendo encerrado...");
        }
    }
}