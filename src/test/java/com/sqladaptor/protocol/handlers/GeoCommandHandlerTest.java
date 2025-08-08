package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.converter.RedisToSqlConverter;
import com.sqladaptor.database.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GeoCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private GeoCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new GeoCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleGeoAddCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("GEOADD",
            Arrays.asList("locations", "13.361389", "38.115556", "Palermo"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleGeoPosCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any()))
            .thenReturn(Arrays.asList("13.361389", "38.115556"));
        
        RedisCommandNode command = new RedisCommandNode("GEOPOS", 
            Arrays.asList("locations", "Palermo"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
    
    @Test
    void testHandleoDistCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("166274.1516");
        
        RedisCommandNode command = new RedisCommandNode("GEODIST",
            Arrays.asList("locations", "Palermo", "Catania"));
        String response = handler.handle(command);
        assertEquals("+166274.1516\r\n", response);
    }
    
    @Test
    void testHandleGeoRadiusCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any()))
            .thenReturn(Arrays.asList("Palermo", "Catania"));
        
        RedisCommandNode command = new RedisCommandNode("GEORADIUS", 
            Arrays.asList("locations", "15", "37", "200", "km"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
    
    @Test
    void testHandleGeoRadiusByMemberCommand() throws Exception {
        when(mockDatabaseManager.executeQueryMultiple(anyString(), any(String[].class)))
                .thenReturn(Arrays.asList(new String[]{"Palermo"}, new String[]{"Catania"}));

        RedisCommandNode command = new RedisCommandNode("GEORADIUSBYMEMBER",
                Arrays.asList("locations", "Palermo", "200", "km"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
}
