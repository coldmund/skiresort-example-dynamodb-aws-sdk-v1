package com.aws.samples.skiresort.test.jyhong;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.aws.samples.skiresort.domain.AvalancheDanger;
import com.aws.samples.skiresort.domain.LiftDynamicStats;
import com.aws.samples.skiresort.domain.LiftStaticStats;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * make table of https://aws.amazon.com/ko/blogs/database/amazon-dynamodb-single-table-design-using-dynamodbmapper-and-spring-boot/
 */
@ExtendWith(LocalDBCreationExtension.class)
@SpringBootTest
public class AddDataTestIT {

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

//    @Test
//    void testSaveResort() {
//        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
//
//        assertEquals(0, dynamoDBIntegrationTestConfiguration.getItemCount());
//        LocalDate now = LocalDate.now();
//        Resort resort = Resort.builder().date(now).build();
//        mapper.save(resort);
//        assertEquals(1, dynamoDBIntegrationTestConfiguration.getItemCount());
//        // cleanup
//        mapper.delete(resort);
//    }

  @Test
  void testAddData() {
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

    // first save a resort
    assertEquals(0, dynamoDBIntegrationTestConfiguration.getItemCount());
    mapper.save(Resort.builder()
        .date(LocalDate.parse("07-03-2021", formatter))
        .totalUniqueLiftRiders(7788)
        .averageSnowCoverageInches(50)
        .avalancheDanger(AvalancheDanger.HIGH)
        .openLifts(60)
        .build());
    mapper.save(Resort.builder()
        .date(LocalDate.parse("08-03-2021", formatter))
        .totalUniqueLiftRiders(6699)
        .averageSnowCoverageInches(40)
        .avalancheDanger(AvalancheDanger.MODERATE)
        .openLifts(60)
        .build());
    mapper.save(Resort.builder()
        .date(LocalDate.parse("09-03-2021", formatter))
        .totalUniqueLiftRiders(5678)
        .averageSnowCoverageInches(65)
        .avalancheDanger(AvalancheDanger.EXTREME)
        .openLifts(53)
        .build());

    // then lift static
    mapper.save(LiftStaticStats.builder()
        .experiencedRidersOnly(true)
        .verticalFeet(1230)
        .liftTime("7:00")
        .liftNumber(1234)
        .build());
    mapper.save(LiftStaticStats.builder()
        .experiencedRidersOnly(false)
        .verticalFeet(2340)
        .liftTime("13:00")
        .liftNumber(6789)
        .build());

    // then lift
    mapper.save(LiftDynamicStats.builder()
        .date(LocalDate.parse("07-03-2021", formatter))
        .totalUniqueLiftRiders(3000)
        .averageSnowCoverageInches(60)
        .avalancheDanger(AvalancheDanger.HIGH)
        .openLifts("OPEN")
        .liftNumber(1234)
        .build());
    mapper.save(LiftDynamicStats.builder()
        .date(LocalDate.parse("08-03-2021", formatter))
        .totalUniqueLiftRiders(3500)
        .averageSnowCoverageInches(50)
        .avalancheDanger(AvalancheDanger.MODERATE)
        .openLifts("OPEN")
        .liftNumber(1234)
        .build());
    mapper.save(LiftDynamicStats.builder()
        .date(LocalDate.parse("08-03-2021", formatter))
        .totalUniqueLiftRiders(4000)
        .averageSnowCoverageInches(60)
        .avalancheDanger(AvalancheDanger.MODERATE)
        .openLifts("OPEN")
        .liftNumber(6789)
        .build());
    mapper.save(LiftDynamicStats.builder()
        .date(LocalDate.parse("09-03-2021", formatter))
        .totalUniqueLiftRiders(2000)
        .averageSnowCoverageInches(88)
        .avalancheDanger(AvalancheDanger.EXTREME)
        .openLifts("OPEN")
        .liftNumber(6789)
        .build());
  }
}
