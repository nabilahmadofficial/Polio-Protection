package com.Polio.Protection.admin.Worker;

public class AdminWorkerModal {

    String worker_id, worker_key, worker_name, worker_cell_number, worker_date_of_birth, worker_gender,
            worker_add_admin_key, worker_add_date, worker_update_admin_key, worker_update_date, worker_team_id;

    public AdminWorkerModal(String worker_id, String worker_key, String worker_name, String worker_cell_number, String worker_date_of_birth, String worker_gender,
                            String worker_add_admin_key, String worker_add_date, String worker_update_admin_key, String worker_update_date, String worker_team_id) {
        this.worker_id = worker_id;
        this.worker_key = worker_key;
        this.worker_name = worker_name;
        this.worker_cell_number = worker_cell_number;
        this.worker_date_of_birth = worker_date_of_birth;
        this.worker_gender = worker_gender;
        this.worker_add_admin_key = worker_add_admin_key;
        this.worker_add_date = worker_add_date;
        this.worker_update_admin_key = worker_update_admin_key;
        this.worker_update_date = worker_update_date;
        this.worker_team_id = worker_team_id;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getWorker_key() {
        return worker_key;
    }

    public void setWorker_key(String worker_key) {
        this.worker_key = worker_key;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getWorker_cell_number() {
        return worker_cell_number;
    }

    public void setWorker_cell_number(String worker_cell_number) {
        this.worker_cell_number = worker_cell_number;
    }

    public String getWorker_date_of_birth() {
        return worker_date_of_birth;
    }

    public void setWorker_date_of_birth(String worker_date_of_birth) {
        this.worker_date_of_birth = worker_date_of_birth;
    }

    public String getWorker_gender() {
        return worker_gender;
    }

    public void setWorker_gender(String worker_gender) {
        this.worker_gender = worker_gender;
    }

    public String getWorker_add_admin_key() {
        return worker_add_admin_key;
    }

    public void setWorker_add_admin_key(String worker_add_admin_key) {
        this.worker_add_admin_key = worker_add_admin_key;
    }

    public String getWorker_add_date() {
        return worker_add_date;
    }

    public void setWorker_add_date(String worker_add_date) {
        this.worker_add_date = worker_add_date;
    }

    public String getWorker_update_admin_key() {
        return worker_update_admin_key;
    }

    public void setWorker_update_admin_key(String worker_update_admin_key) {
        this.worker_update_admin_key = worker_update_admin_key;
    }

    public String getWorker_update_date() {
        return worker_update_date;
    }

    public void setWorker_update_date(String worker_update_date) {
        this.worker_update_date = worker_update_date;
    }

    public String getWorker_team_id() {
        return worker_team_id;
    }

    public void setWorker_team_id(String worker_team_id) {
        this.worker_team_id = worker_team_id;
    }
}
