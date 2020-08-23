package dev.giako.immudb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codenotary.immudb4j.FileRootHolder;
import io.codenotary.immudb4j.ImmuClient;
import io.codenotary.immudb4j.RootHolder;
import io.codenotary.immudb4j.crypto.VerificationException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmudbTest {
    private static List<CriticalFinancialTransaction> txs;

    public static void main(String[] args) {
        txs = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();
        RootHolder fileRootHolder;

        try {
            fileRootHolder = FileRootHolder.newBuilder().setRootsFolder("./giako_roots").build();
        } catch (IOException e) {
            System.err.println("Cannot create roots folder");
            e.printStackTrace();
            return;
        }

        ImmuClient immuClient = ImmuClient.newBuilder()
                .setServerUrl("localhost")
                .setServerPort(3322)
                .setRootHolder(fileRootHolder)
                .build();


        immuClient.login("immudb", "immudb");
        immuClient.createDatabase("giakotxs");
        immuClient.useDatabase("giakotxs");

        System.out.println("Let's store a bunch of critical sensible transactions on Immudb");
        setupTransactionData();

        try {
            setTxsInImmudb(immuClient, om);

            for (int i = 1; i <= 3; i++) {
                String value  = new String(immuClient.safeGet("TX" + i));
                CriticalFinancialTransaction tx = om.readValue(value, CriticalFinancialTransaction.class);
                System.out.println("Safely got: " + tx);
            }
        } catch (VerificationException e) {
            System.err.println("Some tampering may be happening!");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.err.println("Error deserializing class");
            e.printStackTrace();
        }
    }

    private static void setTxsInImmudb(ImmuClient immuClient, ObjectMapper om) {
        ImmudbTest.txs.forEach(tx -> {
            try {
                immuClient.safeSet(tx.getTransactionId(), om.writeValueAsBytes(tx));
            } catch (VerificationException | JsonProcessingException e) {
                System.err.println("Error in serialization");
                e.printStackTrace();
            }
        });
    }

    private static void setupTransactionData() {
        CriticalFinancialTransaction tx1 = CriticalFinancialTransaction.builder()
                .transactionId("TX1")
                .amount(BigDecimal.valueOf(199.99))
                .currency("EUR")
                .beneficiary("John Doe")
                .build();

        CriticalFinancialTransaction tx2 = CriticalFinancialTransaction.builder()
                .transactionId("TX2")
                .amount(BigDecimal.valueOf(1100.00))
                .currency("EUR")
                .beneficiary("Jane Doe")
                .build();

        CriticalFinancialTransaction tx3 = CriticalFinancialTransaction.builder()
                .transactionId("TX3")
                .amount(BigDecimal.valueOf(-349.50))
                .currency("EUR")
                .beneficiary("ACME Corp.")
                .build();

        Collections.addAll(txs, tx1, tx2, tx3);
    }
}
