

import java.io.IOException;

import models.ClassRoom;
import network.serialisation.JsonObjectMapper;
import models.TimeSlot;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

/**
 * This class contains automated tests to validate the serialisation.client and the serialisation.server
 * implementation of the  Daryll Protocol
 *
 * @author Yohann Meyer
 */
public class DataTransferTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public EphemeralClientServerPair pair = new EphemeralClientServerPair();

    private ClassRoom f01 = new ClassRoom("F01");
    private String f01Json = "{\"classRoom\":\"F01\"}";

    private ClassRoom a01 = new ClassRoom("A01");

    private TimeSlot t1 = new TimeSlot("f01", Instant.now().toEpochMilli() , 2);
    private String t1Json = "{\"classroom\":\"F01\",\"time_start\":1025,\"time_end\":1100}";

    @Test
    public void theTestRouletteServerShouldRunDuringTests() throws IOException {
        assertTrue(pair.getServer().isRunning());
    }

    @Test
    public void itShouldBePossibleToCreateJson() throws IOException {
        System.out.println( JsonObjectMapper.toJson( t1 ) );
        assertEquals(f01Json, JsonObjectMapper.toJson(f01));
        assertEquals( t1Json , JsonObjectMapper.toJson( t1 ));

    }

    @Test
    public void itShouldBePossibleToDeserializeJson() throws IOException {

        assertEquals(f01.getClassRoom(), JsonObjectMapper.parseJson(f01Json, ClassRoom.class).getClassRoom());
    }

    @Test 
    public void itShouldBePossibleToTransferAClassRoom() throws IOException {

    }

}
