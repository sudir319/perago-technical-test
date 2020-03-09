package com.perago.techtest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngineImpl;
import com.perago.techtest.DiffRendererImpl;

public class DiffEngineImplTestNew {

    DiffEngineImpl diffEngine = new DiffEngineImpl();
    DiffRendererImpl renderer = new DiffRendererImpl();

    @Test
    public void originalNullAndModifiedFull() throws Exception
    {
        Person originalObject = null;
        
        Person modifiedObject = new Person();
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("originalNullAndModifiedFull");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), modifiedObject);
    }
    
    @Test
    public void originalFullAndModifiedNull() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = null;
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("originalFullAndModifiedNull");
        
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
        System.out.println("addFriend");
        //System.out.printlnrenderer.render(diff));
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
        System.out.println("updatePersonFriendAndPet");
        //System.out.printlnrenderer.render(diff));
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
        System.out.println("updatePersonFriendAndPet");
        //System.out.printlnrenderer.render(diff));
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
}