package com.perago.techtest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngine;
import com.perago.techtest.DiffEngineImpl;
import com.perago.techtest.DiffRenderer;
import com.perago.techtest.DiffRendererImpl;

public class DiffEngineImplTest_Old {

    DiffEngine diffEngine = new DiffEngineImpl();
    DiffRenderer renderer = new DiffRendererImpl();
  
    
    
    @Test
    public void diffEngineCalculatesDiffShouldEqualModifedWhenOriginalIsNull() throws Exception {

        Person person = new Person();
        person.setFirstName("Fred");
        person.setSurname("Smith");
        Person friend = new Person();
        friend.setFirstName("Tom");
        friend.setSurname("Brown");
        person.setFriend(friend);
        Set<String> nickNames = new HashSet<>();
        nickNames.add("schooter");
        nickNames.add("biff");
        person.setNickNames(nickNames);

        Diff<Person> diff = diffEngine.calculate(null, person);
        System.out.println("\ndiffEngineCalculatesDiffShouldEqualModifedWhenOriginalIsNull");
        System.out.println("************************************************************");
        System.out.println(renderer.render(diff));
        assertNotNull(diff);
        
        assertEquals(diff.getHolder(), null);
    }

    @Test
    public void diffEngineApplyShouldReturnModifiedWhenOriginalIsNullAndModifiedNonNull() throws Exception {
        Person modified = new Person();
        modified.setFirstName("Fred");
        modified.setSurname("Smith");

        Diff<Person> diff = diffEngine.calculate(null, modified);
        System.out.println( renderer.render(diff));
        
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
    public void diffEngineApplyShouldReturnNullWhenModifiedIsNull() throws Exception {
        Person original = new Person();
        original.setFirstName("Fred");
        original.setSurname("Smith");

        Diff<Person> diff = diffEngine.calculate(original, null);

        System.out.println("\ndiffEngineApplyShouldReturnNullWhenModifiedIsNull");
        System.out.println("**************************************************");
        System.out.println( renderer.render(diff));
        
        Person applied = diffEngine.apply(original, diff);
        
        assertNotNull(original);
        assertNull(applied);
    }

    @Test
    public void diffShouldContainChangeLogs() throws Exception {
        Person person1 = new Person();
        person1.setFirstName("Fred");
        person1.setSurname("Smith");
        
        Person person2 = new Person();
        person2.setFirstName("Fred");
        person2.setSurname("Jones");
        
        Diff<Person> diff = diffEngine.calculate(person1, person2);
        
        System.out.println("\ndiffShouldContainChangeLogs");
        System.out.println("****************************");
        System.out.println( renderer.render(diff));

        assertNotEquals(0L, diff.getChangeLogs().size());
    }

    @Test
    public void diffApplyShouldWorkOnCollections() throws Exception {
        Person person1 = new Person();
        person1.setFirstName("Fred");
        person1.setSurname("Smith");
        
        Set<String> nickNames = new HashSet<>();
        nickNames.add("scooter");
        nickNames.add("biff");
        person1.setNickNames(nickNames);

        Person person2 = (Person) BeanUtils.cloneBean(person1);
        person1.setFirstName("Fred");
        person1.setSurname("Jones");

        Set<String> names = new HashSet<>();
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