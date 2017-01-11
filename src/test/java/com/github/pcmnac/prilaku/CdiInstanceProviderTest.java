package com.github.pcmnac.prilaku;

import static com.github.pcmnac.prilaku.Pku.$;
import static com.github.pcmnac.prilaku.Pku.registerAnnotated;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import com.github.pcmnac.prilaku.Pku.Enhanced;
import com.github.pcmnac.prilaku.annotation.Behavior;
import com.github.pcmnac.prilaku.annotation.BehaviorOf;
import com.github.pcmnac.prilaku.annotation.Domain;
import com.github.pcmnac.prilaku.test.Log;
import com.github.pcmnac.prilaku.test.WeldJUnit4Runner;

@RunWith(WeldJUnit4Runner.class)
public class CdiInstanceProviderTest
{

    // domain objects
    public static class Account
    {
        public String number;
        public double amount;
    }

    public static class PremiumAccount extends Account
    {
    }

    // printer behavior
    @Behavior
    public static interface Printer
    {
        void print();
    }

    @BehaviorOf(Account.class)
    public static class ConsoleAccountPrinter implements Printer
    {
        @Domain
        private Account account;
        
        @Inject @Log
        private Logger LOG;

        @Override
        public void print()
        {
            System.out.println(account);
            System.out.println("Account: " + account.number);
            LOG.info("Account: " + account.number);
        }

    }

    // serializer behavior

    @Behavior
    public static interface Serializer
    {
        String serialize();
    }

    @BehaviorOf(Account.class)
    public static class JsonAccountSerializer implements Serializer
    {
        @Domain
        Account account;

        @Override
        public String serialize()
        {

            return "{ number: \"" + account.number + "\", amount: " + account.amount + " }";
        }

    }

    @Test
    public void test()
    {

        registerAnnotated("com.github.pcmnac.prilaku");

        Account account = new PremiumAccount();
        account.number = "123456";
        account.amount = 1000.65;

        $(account).get(Printer.class).print();

        System.out.println($(account).get(Serializer.class).serialize());

        Enhanced $account = $(account);
        $account.get(Printer.class).print();
        $account.get(Serializer.class).serialize();
    }

}
