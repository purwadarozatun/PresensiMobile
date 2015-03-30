package mobile.javan.co.id.presensi.model;

import java.util.Date;

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
        if (jamMasuk!=null){
            return true;
        }
        return false;
    }

    private Boolean isPulang() {
        if (jamKeluar!=null){
            return true;
        }
        return false;
    }


    public StaticResponse getStatusKerja(){
        StaticResponse staticResponse = null;
        if(isKerja()){
            staticResponse = new StaticResponse();
            if(jamMasuk.getHours() >= 8){
                staticResponse.responseStatus = "Datang Terlambat";
                staticResponse.status = false;
            }else{
                staticResponse.responseStatus = "Tepat waktu";
                staticResponse.status = true;
            }
        }
        return staticResponse;
    }

    public StaticResponse getStatusPulang(){
        StaticResponse staticResponse = null;
        if(isPulang()){
            if(getDurasiKerja() >= 9){
                staticResponse.responseStatus = "Jam Kerja Cukup";
                staticResponse.status = true;
            }else{
                staticResponse.responseStatus = "Pulang Cepat";
                staticResponse.status = false;
            }
        }
        return staticResponse;
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

