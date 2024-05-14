package com.aws.samples.skiresort.test.jyhong;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.aws.samples.skiresort.test.LocalDBCreationExtension;
import com.aws.samples.skiresort.test.config.DynamoDBIntegrationTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@ExtendWith(LocalDBCreationExtension.class)
@SpringBootTest
public class AddGsiTestIT {

  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  @Autowired
  private DynamoDBIntegrationTestConfiguration dynamoDBIntegrationTestConfiguration;

  @BeforeEach
  public void initDataModel() {
    dynamoDBIntegrationTestConfiguration.dynamoDBLocalSetup();
  }

  @Test
  void  testAddGsi() {

    try {
      List<KeySchemaElement> schema = List.of(
          new KeySchemaElement().withAttributeName("GSI_2_PK").withKeyType(KeyType.HASH),
          new KeySchemaElement().withAttributeName("GSI_2_SK").withKeyType(KeyType.RANGE)
      );
      CreateGlobalSecondaryIndexAction action = new CreateGlobalSecondaryIndexAction()
          .withIndexName("GSI_2")
          .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
          .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
          .withKeySchema(schema);
      GlobalSecondaryIndexUpdate index = new GlobalSecondaryIndexUpdate().withCreate(action);
      UpdateTableRequest request = new UpdateTableRequest()
          .withTableName("SkiLifts")
          .withAttributeDefinitions(
              new AttributeDefinition("GSI_2_PK", ScalarAttributeType.S),
              new AttributeDefinition("GSI_2_SK", ScalarAttributeType.S)
          )
          .withGlobalSecondaryIndexUpdates(index);
      amazonDynamoDB.updateTable(request);
    } catch (AmazonServiceException e) {
      System.out.println(e.getRawResponseContent());
    }
  }
}
