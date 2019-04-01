package Misc;

import android.widget.Button;

public class RegalButton {
    private Button btn;
    private Regal regal;
    public RegalButton(Button btn, Regal regal){
        this.btn = btn;
        this.regal = regal;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public Regal getRegal() {
        return regal;
    }

    public void setRegal(Regal regal) {
        this.regal = regal;
    }
}
