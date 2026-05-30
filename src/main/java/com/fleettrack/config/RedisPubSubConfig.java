package com.fleettrack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleettrack.pubsub.AlertSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.*;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import java.util.concurrent.Executor;

@Configuration
public class RedisPubSubConfig {

    public static final String MAINTENANCE_ALERT_CHANNEL = "fleet.maintenance.alert";
    public static final String VEHICLE_OFFLINE_CHANNEL = "fleet.vehicle.offline";

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            RedisConnectionFactory connectionFactory,
            Executor taskExecutor,
            MessageListenerAdapter maintenanceListenerAdapter,
            MessageListenerAdapter vehicleOfflineListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(taskExecutor);

        container.addMessageListener(maintenanceListenerAdapter, new ChannelTopic(MAINTENANCE_ALERT_CHANNEL));
        container.addMessageListener(vehicleOfflineListenerAdapter, new ChannelTopic(VEHICLE_OFFLINE_CHANNEL));

        return container;
    }

    @Bean
    public MessageListenerAdapter maintenanceListenerAdapter(AlertSubscriber alertSubscriber) {
        // Serializer təyin etmirik, default olaraq String/Byte qəbul edəcək
        return new MessageListenerAdapter(alertSubscriber, "onMaintenanceAlert");
    }

    @Bean
    public MessageListenerAdapter vehicleOfflineListenerAdapter(AlertSubscriber alertSubscriber) {
        return new MessageListenerAdapter(alertSubscriber, "onVehicleOfflineAlert");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}