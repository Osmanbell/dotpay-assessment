package com.example.dotpayassessment.utils;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

/**
 * Created by @oladapoyuken 23/03/2022
 */
@Component
public class RandomAlphaNumericGenerator {


    public String alpha(int n){
        RandomStringGenerator generator = new RandomStringGenerator.Builder().
                withinRange('A', 'Z').build();
        return generator.generate(n);
    }

    public String numeric(int n){
        RandomStringGenerator generator = new RandomStringGenerator.Builder().
                withinRange('0', '9').build();
        return generator.generate(n);
    }
}
