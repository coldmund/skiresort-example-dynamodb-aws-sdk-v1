/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.aws.samples.skiresort.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "SkiLifts")
@NoArgsConstructor
public class LiftStaticStats {

    @DynamoDBAttribute(attributeName = "ExperiencedRidersOnly")
    private boolean experiencedRidersOnly;

    @DynamoDBAttribute(attributeName = "VerticalFeet")
    private int verticalFeet;

    @DynamoDBAttribute(attributeName = "LiftTime")
    private String liftTime;

    @DynamoDBAttribute(attributeName = "LiftNumber")
    private int liftNumber;

    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return "LIFT#" + liftNumber;
    }

    public void setPK(String pk) {
        // intentionally left blank: PK is set by setting liftNumber attribute
    }

    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return "STATIC_DATA";
    }

    public void setSK(String sk) {
        // intentionally left blank: SK is static data
    }

    @DynamoDBIndexHashKey(attributeName = "GSI_2_PK", globalSecondaryIndexName = "GSI_2")
    public String getGSI2PK() {
        return "STATIC_DATA";
    }

    public void setGSI2PK(String gsi2Pk) {
        // intentionally left blank: PK is set by setting liftNumber attribute
    }

    @DynamoDBIndexRangeKey(attributeName = "GSI_2_SK", globalSecondaryIndexName = "GSI_2")
    public String getGSI2SK() {
        return "LIFT#" + liftNumber;
    }

    public void setGSI2SK(String gsi2Sk) {
        // intentionally left blank: SK is set by setting the totalUniqueLiftRiders attribute
    }
}