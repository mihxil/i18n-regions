package org.meeuw.i18n.test;

import org.teavm.jso.dom.html.HTMLDocument;

public class Client {
    public static void main(String[] args) {
        var document = HTMLDocument.current();
        var div = document.createElement("div");
        div.appendChild(document.createTextNode("TeaVM generated element"));
        document.getBody().appendChild(div);
        //RegionService regionService = new RegionService(document);
    }
}
