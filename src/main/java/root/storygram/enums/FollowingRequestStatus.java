package root.storygram.enums;

public enum FollowingRequestStatus {
    PENDING,    // Запрос отправлен, ожидает подтверждения
    ACCEPTED,   // Запрос одобрен, подписка оформлена
    BLOCKED,    // Пользователь заблокирован
}
