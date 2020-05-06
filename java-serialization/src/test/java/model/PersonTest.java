package model;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    public void whenSerializingAndDeserializing_ThenObjectIsTheSame() throws IOException, ClassNotFoundException {
        Person person = new Person(28, "Fellipe Souto", "fellipe@caelum.com", 178);

        String fileName = "data.txt";
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(person);
        objectOutputStream.flush();
        objectOutputStream.close();

        fileOutputStream.flush();
        fileOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Person readedPerson = (Person) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();

        assertEquals(readedPerson.getName(), person.getName());
        assertEquals(readedPerson.getEmail(), person.getEmail());
        assertEquals(readedPerson.getAge(), person.getAge());

        // height é transient, então não é serializado
        assertNotEquals(readedPerson.getHeight(), person.getHeight());

    }

}