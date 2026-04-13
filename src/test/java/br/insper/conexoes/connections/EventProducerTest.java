package br.insper.conexoes.connections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventProducerTest {

    @Mock
    private RedisTemplate<String, Event> redisTemplate;

    @Mock
    private ListOperations<String, Event> listOperations;

    @InjectMocks
    private EventProducer eventProducer;

    @Test
    void send_shouldPushEventToQueue() {
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        Event event = new Event("TYPE", "description", "SOURCE");
        eventProducer.send(event);

        verify(listOperations).leftPush("events-queue", event);
    }
}
