package lcsimlib.gui;

import javax.swing.JLabel;
import lcsimlib.Register;

public class RegisterLabel extends JLabel
{

    private Register reg;
    public RegisterLabel(Register register)
    {
        super();
        this.setFont(AppFont.getMonoFont());
        reg = register;
    }
    public Register getRegister()
    {
        return reg;
    }
    public void update()
    {
        String regName = reg.getName();
        String hexFormat = "x%0" + (reg.getSize()*2) + "X";
        String hexVal = "";
        String decVal = "";
        switch(reg.getSize())
        {
        case 1:
            byte val1 = reg.peek1Bytes();
            hexVal = String.format(hexFormat, val1);
            decVal = "" + val1;
            break;
        case 2:
            short val2= reg.peek2Bytes();
            hexVal = String.format(hexFormat, val2);
            decVal = "" + val2;
            break;
        case 4:
            int val4 = reg.peek4Bytes();
            hexVal = String.format(hexFormat, val4);
            decVal = "" + val4;
            break;
        case 8:
            long val8 = reg.peek8Bytes();
            hexVal = String.format(hexFormat, val8);
            decVal = "" + val8;
            break;
        }
        String setText = regName + "   " + hexVal + "    " + decVal;
        this.setText(setText);
    }

}
