package com.perago.techtest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngineImpl;
import com.perago.techtest.DiffRendererImpl;

public class DiffEngineImplTest {

    DiffEngineImpl diffEngine = new DiffEngineImpl();
    DiffRendererImpl renderer = new DiffRendererImpl();

    @Test
    public void originalNullAndModifiedNotNull() throws Exception
    {
        Person originalObject = null;
        
        Person modifiedObject = new Person();
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n******************************");
        System.out.println("originalNullAndModifiedNotNull");
        System.out.println(renderer.render(diff));
        
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void originalNotNullAndModifiedNull() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = null;
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n******************************");
        System.out.println("originalNotNullAndModifiedNull");
        
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }

    @Test
    public void modifiedSurname() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Jones");
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n***************");
        System.out.println("modifiedSurname");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void addFriend() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        
        modifiedObject.setFriend(friend);
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("*********");
        System.out.println("addFriend");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void updatePersonFriendAndPet() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        originalObject.setFriend(friend);

        Pet pet = new Pet();
        pet.setType("Dog");
        pet.setName("Rover");
        originalObject.setPet(pet);
        
        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Jones");
        
        friend = new Person();
        friend.setFirstName("Jim");
        friend.setSurname("Brown");
        modifiedObject.setFriend(friend);

        pet = new Pet();
        pet.setType("Dog");
        pet.setName("Brown");
        modifiedObject.setPet(pet);
        
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n************************");
        System.out.println("updatePersonFriendAndPet");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void updatePersonFriend() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        originalObject.setFriend(friend);

        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        friend = new Person();
        friend.setFirstName("Jim");
        friend.setSurname("Brown");
        modifiedObject.setFriend(friend);

        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n************************");
        System.out.println("updatePersonFriendAndPet");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void deleteFriend() throws Exception
    {
    	Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        originalObject.setFriend(friend);

        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("John");
        modifiedObject.setSurname("Smith");
        
        modifiedObject.setFriend(null);

        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("deleteFriend");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void updatePersonNickNames() throws Exception
    {
    	Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Set<String> nickNames = new HashSet<String>();
        nickNames.add("scooter");
        nickNames.add("biff");
        
        originalObject.setNickNames(nickNames);
        
        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        nickNames = new HashSet<String>();
        nickNames.add("biff");
        nickNames.add("polly");
        
        modifiedObject.setNickNames(nickNames);

        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\n\n*********************");
        System.out.println("updatePersonNickNames");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
}