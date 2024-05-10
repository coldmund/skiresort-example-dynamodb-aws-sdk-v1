package com.aws.samples.skiresort.test.jyhong;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.aws.samples.skiresort.domain.LiftStaticStats;
import com.aws.samples.skiresort.test.LocalDBCreationExtension;
import com.aws.samples.skiresort.test.config.DynamoDBIntegrationTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@ExtendWith(LocalDBCreationExtension.class)
@SpringBootTest
public class GsiTest {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBIntegrationTestConfiguration dynamoDBIntegrationTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        dynamoDBIntegrationTestConfiguration.dynamoDBLocalSetup();
    }

    @Test
    void    testGetByGsi() {

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

        List<LiftStaticStats> results = mapper.query(LiftStaticStats.class,
                new DynamoDBQueryExpression<LiftStaticStats>()
                        .withConsistentRead(false)
                        .withExpressionAttributeValues(Map.of(":val1", new AttributeValue().withS("STATIC_DATA")))
                        .withIndexName("GSI_2")
                        .withKeyConditionExpression("GSI_2_PK = :val1"));
        results.forEach(System.out::println);
    }
}
