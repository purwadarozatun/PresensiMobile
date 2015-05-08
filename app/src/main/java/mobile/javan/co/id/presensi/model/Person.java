package mobile.javan.co.id.presensi.model;

import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

import mobile.javan.co.id.presensi.model.adapter.result.StaticResultAdapter;
import mobile.javan.co.id.presensi.util.Statics;

/**
 * Created by Purwa on 20/03/2015.
 */
public class Person {
    private String nama;
    private String nik = "";
    private Date jamMasuk;
    private Date jamKeluar;
    private Date jamKerja;
    private Integer durasiKerja = 1;
    private Boolean terlambat = false;
    private String izin;

    public Person(String nama, String nik, Date jamMasuk, Date jamKeluar, Date jamKerja, Integer durasiKerja, Boolean terlambat, String izin) {
        this.nama = nama;
        this.nik = nik;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar;
        this.jamKerja = jamKerja;
        this.durasiKerja = durasiKerja;
        this.terlambat = terlambat;
        this.izin = izin;
    }

    public Person() {
    }

    private Boolean isKerja() {
        if (jamMasuk != null) {
            return true;
        }
        return false;
    }

    private Boolean isPulang() {
        if (jamKeluar != null) {
            return true;
        }
        return false;
    }


    public StaticResultAdapter getStatusKerja() {
        StaticResultAdapter staticResultAdapter = null;

        if (isKerja()) {

            staticResultAdapter = new StaticResultAdapter();
            Integer jam = Integer.parseInt(new Statics().getStringFrom("H", jamMasuk, "0"));
            if (jam == 0) {
                jam = 12;
            }
            if (jam >= 8) {
                staticResultAdapter.responseStatus = "Datang Terlambat";
                staticResultAdapter.status = false;
            } else {
                staticResultAdapter.responseStatus = "Tepat waktu";
                staticResultAdapter.status = true;
            }
        }
        return staticResultAdapter;
    }

    public StaticResultAdapter getStatusPulang() {
        StaticResultAdapter staticResultAdapter = null;
        if (isPulang()) {
            staticResultAdapter = new StaticResultAdapter();
            if (getDurasiKerja() >= 9) {
                staticResultAdapter.responseStatus = "Jam Kerja Cukup";
                staticResultAdapter.status = true;
            } else {
                staticResultAdapter.responseStatus = "Pulang Cepat";
                staticResultAdapter.status = false;
            }
        }
        return staticResultAdapter;
    }


    public Person(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public Integer getDurasiKerja() {
        return durasiKerja;
    }

    public void setDurasiKerja(Integer durasiKerja) {
        this.durasiKerja = durasiKerja;
    }

    public Boolean getTerlambat() {
        return terlambat;
    }

    public void setTerlambat(Boolean terlambat) {
        this.terlambat = terlambat;
    }

    public String getIzin() {
        return izin;
    }

    public void setIzin(String izin) {
        this.izin = izin;
    }

    public Date getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(Date jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public Date getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(Date jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public Date getJamKerja() {
        return jamKerja;
    }

    public void setJamKerja(Date jamKerja) {
        this.jamKerja = jamKerja;
    }


}

