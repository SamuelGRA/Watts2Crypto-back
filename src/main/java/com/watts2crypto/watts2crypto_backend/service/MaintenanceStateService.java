package com.watts2crypto.watts2crypto_backend.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class MaintenanceStateService {

	private final AtomicInteger activeRefreshes = new AtomicInteger(0);
	private final Set<String> activeRefreshesSet = ConcurrentHashMap.newKeySet();

	public void beginRefresh(String operation) {
		activeRefreshes.incrementAndGet();
		activeRefreshesSet.add(operation);
	}

	public void endRefresh(String operation) {
		activeRefreshes.updateAndGet((current) -> Math.max(0, current - 1));
		activeRefreshesSet.remove(operation);
	}

	public boolean isRefreshing() {
		return activeRefreshes.get() > 0;
	}

	public List<String> getActiveRefreshes() {
		return List.copyOf(activeRefreshesSet);
	}
}