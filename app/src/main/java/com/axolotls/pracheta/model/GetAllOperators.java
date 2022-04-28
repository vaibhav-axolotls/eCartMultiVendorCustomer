package com.axolotls.pracheta.model;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllOperators {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("operators")
    @Expose
    private ArrayList<Operator> operators = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Operator> getOperators() {
        return operators;
    }

    public void setOperators(ArrayList<Operator> operators) {
        this.operators = operators;
    }

    public class Operator {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("operator_name")
        @Expose
        private String operatorName;
        @SerializedName("operator_code")
        @Expose
        private String operatorCode;
        @SerializedName("instructions")
        @Expose
        private String instructions;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("operator_image")
        @Expose
        private String operator_image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getOperatorCode() {
            return operatorCode;
        }

        public void setOperatorCode(String operatorCode) {
            this.operatorCode = operatorCode;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOperator_image() {
            return operator_image;
        }

        public void setOperator_image(String operator_image) {
            this.operator_image = operator_image;
        }

    }
}