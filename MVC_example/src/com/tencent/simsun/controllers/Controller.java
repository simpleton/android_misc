package com.tencent.simsun.controllers;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;

import java.util.ArrayList;
import java.util.List;

/**
 * consider remote system design, use list simulate {@link RemoteCallbackList}
 * @author simsun
 */
public abstract class Controller {
	
	@SuppressWarnings("unused")
	
	private static final String TAG = Controller.class.getSimpleName();
	private final List<Handler> outboxHandlers = new ArrayList<Handler>();
	

	public Controller() { }
	
	public void dispose() { }
	
	//should override
	abstract public boolean handleMessage(int what, Object data) ;
    

	public boolean handleMessage(int what) {
		return handleMessage(what, null);
	}
	
	public final void addOutboxHandler(Handler handler) {
		outboxHandlers.add(handler);
	}

	public final void removeOutboxHandler(Handler handler) {
		outboxHandlers.remove(handler);
	}
	
	protected final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
		if (!outboxHandlers.isEmpty()) {
			for (Handler handler : outboxHandlers) {
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	}
}
