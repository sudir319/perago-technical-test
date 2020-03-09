package com.perago.techtest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngineImpl;
import com.perago.techtest.DiffRendererImpl;

public class DiffEngineImplTest {

    DiffEngineImpl diffEngine = new DiffEngineImpl();
    DiffRendererImpl renderer = new DiffRendererImpl();

    @Test
    //Case 1 from the Examples
    public void originalNullAndModifiedNotNull() throws Exception
    {
        Person originalObject = null;
        
        Person modifiedObject = new Person();
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Smith");
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\noriginalNullAndModifiedNotNull");
        System.out.println("******************************");
        System.out.println(renderer.render(diff));
        
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    //Case 2 from the Examples
    public void originalNotNullAndModifiedNull() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = null;
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\noriginalNotNullAndModifiedNull");
        System.out.println("******************************");
        
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }

    @Test
    //Case 3 from the Examples
    public void modifiedSurname() throws Exception
    {
        Person originalObject = new Person();;
        originalObject.setFirstName("Fred");
        originalObject.setSurname("Smith");
        
        Person modifiedObject = new Person();;
        modifiedObject.setFirstName("Fred");
        modifiedObject.setSurname("Jones");
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\nmodifiedSurname");
        System.out.println("***************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
        //diffShouldContainChangeLogs
        assertNotEquals(0L, diff.getChangeLogs().size());
    }
    
    @Test
    //Case 4 from the Examples
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
        System.out.println("\naddFriend");
        System.out.println("*********");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }

    @Test
    //Case 5 from the Examples
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
        System.out.println("\nupdatePersonFriendAndPet");
        System.out.println("************************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    //Case 6 from the Examples
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
        System.out.println("\nupdatePersonFriendAndPet");
        System.out.println("************************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    //Case 7 from the Examples
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
        System.out.println("\ndeleteFriend");
        System.out.println("************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    //Case 8 from the Examples
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
        System.out.println("\nupdatePersonNickNames");
        System.out.println("*********************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void diffMustReflectAllInformationThatWasCreatedUpdatedAndDeleted() throws Exception
    {
        Person originalObject = new Person();
        originalObject.setFirstName("Fred");
        originalObject.setFirstName("Smith");
        
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        originalObject.setFriend(friend);
        
        Person modifiedObject = new Person();
        modifiedObject.setFirstName("Fred");
        modifiedObject.setFirstName("Jones");
        
        modifiedObject.setFriend(null);
        
        Pet pet = new Pet();
        pet.setType("Dog");
        pet.setName("Rover");
        
        modifiedObject.setPet(pet);
        
        Diff<Person> diff = diffEngine.calculate(originalObject, modifiedObject);
        System.out.println("\ndiffMustReflectAllInformationThatWasCreatedUpdatedAndDeleted");
        System.out.println("***************************************************************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        assertEquals(diff.getHolder(), originalObject);
    }
    
    @Test
    public void diffEngineApplyShouldReturnModifiedWhenOriginalIsNullAndModifiedNonNull() throws Exception {
        Person modified = new Person();
        modified.setFirstName("Fred");
        modified.setSurname("Smith");

        Diff<Person> diff = diffEngine.calculate(null, modified);
        
        System.out.println("\ndiffEngineApplyShouldReturnModifiedWhenOriginalIsNullAndModifiedNonNull");
        System.out.println("*************************************************************************");
        
        System.out.println(renderer.render(diff));
        
        Person clone = (Person)BeanUtils.cloneBean(modified);
        clone.setSurname("Brown");
        
        Person applied = diffEngine.apply(clone, diff);
        
        assertNotNull(applied);
        assertEquals(applied.getSurname(), modified.getSurname());

    }

    @Test
    public void diffApplyShouldWorkOnCollections() throws Exception {
        Person person1 = new Person();
        person1.setFirstName("Fred");
        person1.setSurname("Smith");
        
        Set<String> nickNames = new HashSet<String>();
        nickNames.add("scooter");
        nickNames.add("biff");
        person1.setNickNames(nickNames);

        Person person2 = (Person) BeanUtils.cloneBean(person1);
        person1.setFirstName("Fred");
        person1.setSurname("Jones");

        Set<String> names = new HashSet<String>();
        names.add("biff");
        names.add("polly");
        person2.setNickNames(names);

        Diff<Person> diff = diffEngine.calculate(person1, person2);
        
        System.out.println("\ndiffApplyShouldWorkOnCollections");
        System.out.println("**********************************");
        System.out.println( renderer.render(diff));
        Person applied = diffEngine.apply(person1, diff);
        
        assertEquals(applied.getSurname(),person2.getSurname());
        assertEquals(applied.getFirstName(),person2.getFirstName());
        assertEquals(applied.getNickNames(),person2.getNickNames());
    }
}