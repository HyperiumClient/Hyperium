package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IStringVisitor;

import org.cef.callback.CefStringVisitor;

public class StringVisitor implements CefStringVisitor {
    
    IStringVisitor isv;
    
    public StringVisitor(IStringVisitor isv) {
        this.isv = isv;
    }

    @Override
    public void visit(String string) {
        isv.visit(string);
    }

}
