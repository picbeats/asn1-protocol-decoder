import com.lunohod.asn.messages.Group;
import com.lunohod.asn.messages.Person;
import com.lunohod.messages.UperCoderFactory;
import com.lunohod.messages.Asn1Decoder;
import com.lunohod.messages.Asn1Encoder;
import org.junit.*;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Collection;

public class TestEncodeDecode {

    @Test
    public void encodeDecodeGroup() throws Exception {
        Person person = new Person();
        person.setName("MyName");
        person.setAge(44);

        Group group = new Group();

        ArrayList<Person> people = new ArrayList<>();
        people.add(person);
        group.setPersons(people);
        group.setGroupName("Family");


        UperCoderFactory coderFactory = new UperCoderFactory();

        Asn1Encoder encoder = new Asn1Encoder(coderFactory);

        byte[] bytes = encoder.encode(group);

        System.out.println("Group: " + DatatypeConverter.printHexBinary(bytes));

        Asn1Decoder asn1Decoder = new Asn1Decoder(coderFactory);

        Group result = (Group) asn1Decoder.decode(Group.class, bytes);

        Assert.assertNotNull(result);
        Assert.assertEquals(group.getGroupName(), result.getGroupName());
        Collection<Person> people1 = result.getPersons();
        Assert.assertNotNull(people1);
        Assert.assertEquals(1, people1.size());


        Person[] pesonArray = new Person[people1.size()];
        people1.toArray(pesonArray);
        Person person1 = pesonArray[0];
        Assert.assertEquals(person.getAge(), person1.getAge());
        Assert.assertEquals(person.getName(), person1.getName());
    }
}
