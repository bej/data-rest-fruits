package de.derjonk;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by bej on 23/03/2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppleOrOrangeRelProvider implements RelProvider {

    @PostConstruct
    public void foo() {
        System.out.println("created relprovider");
    }

    @Override
    public String getItemResourceRelFor(final Class<?> aClass) {
        return "fruits";
    }

    @Override
    public String getCollectionResourceRelFor(final Class<?> aClass) {
        return "fruits";
    }

    @Override
    public boolean supports(final Class<?> aClass) {
        return Apple.class.isAssignableFrom(aClass) || Orange.class.isAssignableFrom(aClass);
    }
}
