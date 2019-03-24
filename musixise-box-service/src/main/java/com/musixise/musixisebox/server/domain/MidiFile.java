package com.musixise.musixisebox.server.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mu_midi_file")
public class MidiFile extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file")
    private String file;

    @Column(name = "md5")
    private String md5;

    @Column(name = "machine_num")
    private Integer machineNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(Integer machineNum) {
        this.machineNum = machineNum;
    }
}
