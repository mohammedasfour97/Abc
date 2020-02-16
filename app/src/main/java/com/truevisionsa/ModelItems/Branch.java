package com.truevisionsa.ModelItems;

public class Branch {

    private String branch_id , branch_name , branch_uuid , branch_ip4 , company_id ;

    public Branch(String branch_id, String branch_name, String branch_uuid, String branch_ip4, String company_id) {
        this.branch_id = branch_id;
        this.branch_name = branch_name;
        this.branch_uuid = branch_uuid;
        this.branch_ip4 = branch_ip4;
        this.company_id = company_id;
    }

    public Branch() {
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_uuid() {
        return branch_uuid;
    }

    public void setBranch_uuid(String branch_uuid) {
        this.branch_uuid = branch_uuid;
    }

    public String getBranch_ip4() {
        return branch_ip4;
    }

    public void setBranch_ip4(String branch_ip4) {
        this.branch_ip4 = branch_ip4;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    @Override
    public String toString() {
        return branch_name;
    }
}
