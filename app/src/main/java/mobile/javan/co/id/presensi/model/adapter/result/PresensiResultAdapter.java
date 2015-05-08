package mobile.javan.co.id.presensi.model.adapter.result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.service.ConnectionFragment;

/**
 * Created by Purwa on 20/03/2015.
 */
public class PresensiResultAdapter {
    private List<Person> result;


    public List<Person> getResult() {
        if (result == null) {
            result = new ArrayList<Person>();

        }
        return result;
    }

    public void setResult(List<Person> result) {
        this.result = result;
    }

    public PresensiResultAdapter() {
    }

    public Person getPersonByNik(String nik , Date tanggal) {
        ConnectionFragment connectionFragment = new ConnectionFragment();

        this.result = connectionFragment.getPresensis(nik , tanggal).getResult();
        if (this.result.size() >= 1) {
            return this.result.get(0);
        }
        return null;
    }

    public PresensiResultAdapter(List<Person> result) {
        this.result = result;
    }
}
