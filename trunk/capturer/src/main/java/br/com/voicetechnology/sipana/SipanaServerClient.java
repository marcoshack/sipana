package br.com.voicetechnology.sipana;

import br.com.voicetechnology.sipana.sip.SipTransaction;

public interface SipanaServerClient {
    public abstract void addTransaction(SipTransaction transaction);
}
