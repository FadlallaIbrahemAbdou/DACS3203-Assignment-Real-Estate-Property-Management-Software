package realestate.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_MS = 60_000;

    private static final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

    public static boolean isBlocked(String username) {
        Long lockTime = lockTimeCache.get(username);
        if (lockTime == null)
            return false;
        if (System.currentTimeMillis() - lockTime > LOCK_TIME_MS) {
            attemptsCache.remove(username);
            lockTimeCache.remove(username);
            return false;
        }
        return true;
    }

    public static void recordFailedAttempt(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(username, System.currentTimeMillis());
        }
    }

    public static void resetAttempts(String username) {
        attemptsCache.remove(username);
        lockTimeCache.remove(username);
    }

    public static int getRemainingAttempts(String username) {
        int used = attemptsCache.getOrDefault(username, 0);
        return Math.max(0, MAX_ATTEMPTS - used);
    }

}
