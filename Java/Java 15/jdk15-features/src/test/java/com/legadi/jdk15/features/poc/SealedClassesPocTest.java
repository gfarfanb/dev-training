package com.legadi.jdk15.features.poc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/*
 * # Hierarchy with sealed classes
 *                                                                                    
 *                                +---------------+                                   
 *                                |  <<sealed>>   |                                   
 *          +-------------------->| <<interface>> |<------------------+               
 *          |                     |  SpringClass  |                   |               
 *      implements                +--^-------- ^--+                implements         
 *          |                        |         |                      |               
 *          |                   implements   implements               |               
 *   +------+---------+   +----------+---+   +-+---------------+   +--+-------------+ 
 *   |   <<sealed>>   |   |   <<final>>  |   |    <<final>>    |   | <<non-sealed>> | 
 *   |  <<abstract>>  |   |  <<class>>   |   |    <<class>>    |   |   <<class>>    | 
 *   | ComponentClass |   | ServiceClass |   | RepositoryClass |   |   BeanClass    | 
 *   +------^---------+   +--------------+   +-----------------+   +--^-------------+ 
 *          |                                                         |               
 *        extends                                                     |               
 *  +-------+----------+                                              |               
 *  |    <<sealed>>    |                                              |               
 *  |    <<class>>     |                                              |               
 *  | ControllerClass  |                                              |               
 *  +-------^----------+                                              |               
 *          |                                                         |               
 *        extends                                                     |               
 * +--------+-------------+                                         +-+------------+  
 * |     <<final>>        |                                         |  <<class>>   |  
 * |     <<class>>        |                                         | AnotherClass |  
 * | RestControllerClass  |                                         +--------------+  
 * +----------------------+                                                           
 */
@Tag("JEP-360")
@Tag("Preview")
public class SealedClassesPocTest {

    @Test
    public void sealedClasses_longClassInheritanceInstantiation_implementation() {
        RestControllerClass instance = new RestControllerClass();

        assertThat(instance, instanceOf(SpringClass.class));
        assertThat(instance, instanceOf(ComponentClass.class));
        assertThat(instance, instanceOf(ControllerClass.class));
        assertThat(instance, not(instanceOf(ServiceClass.class)));
        assertThat(instance, not(instanceOf(RepositoryClass.class)));
        assertThat(instance, not(instanceOf(BeanClass.class)));
    }

    @Test
    public void sealedClasses_immediateInheritance_implementation() {
        ServiceClass instanceA = new ServiceClass();
        RepositoryClass instanceB = new RepositoryClass();

        assertThat(instanceA, instanceOf(SpringClass.class));
        assertThat(instanceA, not(instanceOf(ComponentClass.class)));
        assertThat(instanceA, not(instanceOf(RepositoryClass.class)));
        assertThat(instanceA, not(instanceOf(BeanClass.class)));
        
        assertThat(instanceB, instanceOf(SpringClass.class));
        assertThat(instanceB, not(instanceOf(ComponentClass.class)));
        assertThat(instanceB, not(instanceOf(ServiceClass.class)));
        assertThat(instanceB, not(instanceOf(BeanClass.class)));
    }

    @Test
    public void sealedClasses_nonSealedAllowsInheritance_implementation() {
        AnotherClass instance = new AnotherClass();

        assertThat(instance, instanceOf(SpringClass.class));
        assertThat(instance, instanceOf(BeanClass.class));
        assertThat(instance, not(instanceOf(ComponentClass.class)));
        assertThat(instance, not(instanceOf(ServiceClass.class)));
        assertThat(instance, not(instanceOf(RepositoryClass.class)));
    }

    @Test
    public void sealedClasses_anonymousnonSealedInstance_implementation() {
        BeanClass instance = new BeanClass() {};

        assertThat(instance, instanceOf(SpringClass.class));
        assertThat(instance, instanceOf(BeanClass.class));
        assertThat(instance, not(instanceOf(AnotherClass.class)));
        assertThat(instance, not(instanceOf(ComponentClass.class)));
        assertThat(instance, not(instanceOf(ServiceClass.class)));
        assertThat(instance, not(instanceOf(RepositoryClass.class)));
    }

    @Test
    public void sealedClasses_instanceOf_implementation() {
        Object instance = new RestControllerClass();

        if(instance instanceof SpringClass springInstance) {
            assertThat(springInstance, equalTo(instance));
        }

        if(instance instanceof ComponentClass componentInstance) {
            assertThat(componentInstance, equalTo(instance));
        }

        if(instance instanceof ControllerClass controllerInstance) {
            assertThat(controllerInstance, equalTo(instance));
        }
    }

    static sealed interface SpringClass
            permits ComponentClass, ServiceClass, RepositoryClass, BeanClass {

    }

    static sealed abstract class ComponentClass implements SpringClass
            permits ControllerClass {

    }

    static sealed class ControllerClass extends ComponentClass
            permits RestControllerClass {

    }

    static final class RestControllerClass extends ControllerClass {

    }

    static final class ServiceClass implements SpringClass {

    }

    static final class RepositoryClass implements SpringClass {

    }

    static non-sealed abstract class BeanClass implements SpringClass {

    }

    static class AnotherClass extends BeanClass {

    }
}
