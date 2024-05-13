package com.aws.samples.skiresort.test.jyhong;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.aws.samples.skiresort.domain.LiftDynamicStats;
import com.aws.samples.skiresort.domain.Resort;
import com.aws.samples.skiresort.test.LocalDBCreationExtension;
import com.aws.samples.skiresort.test.config.DynamoDBIntegrationTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@ExtendWith(LocalDBCreationExtension.class)
@SpringBootTest
public class ConfirmDataTestIT {

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  @Autowired
  private DynamoDBIntegrationTestConfiguration dynamoDBIntegrationTestConfiguration;

  @BeforeEach
  public void initDataModel() {
    dynamoDBIntegrationTestConfiguration.dynamoDBLocalSetup();
  }

//    @AfterEach
//    public void verifyEmptyDatabase() {
//        assertEquals(0, dynamoDBIntegrationTestConfiguration.getItemCount());
//    }

  @Test
  void testGetResortData() {
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

    // sort key is mandatory for load
    System.out.println("----- resort 07-03-2021 -----");
    System.out.println(
        mapper.load(Resort.builder().date(LocalDate.parse("07-03-2021", formatter)).build())
    );

    System.out.println("----- resort data -----");
    QueryResult result = amazonDynamoDB.query(new QueryRequest()
        .withTableName("SkiLifts")
        .withKeyConditionExpression("PK = :v_pk")
        .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue("RESORT_DATA"))));
    System.out.println(result.toString());
  }

  @Test
  void    testGetLiftData() {

    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

    // lift '1234' dynamic data
    System.out.println("----- lift 1234 dynamic data -----");
    System.out.println(
        mapper.load(LiftDynamicStats.builder().liftNumber(1234).date(LocalDate.parse("07-03-2021", formatter)).build())
    );

    // lift '1234' all
    System.out.println("----- lift 1234 all -----");
    QueryResult result = amazonDynamoDB.query(new QueryRequest()
        .withTableName("SkiLifts")
        .withKeyConditionExpression("PK = :v_pk")
        .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue("LIFT#1234"))));
    System.out.println(result.toString());
  }
}
