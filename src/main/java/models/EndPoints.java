package models;

public class EndPoints {
    // создание курьера
    public static final String CREATE_COURIER_PATH = "/api/v1/courier";
    // логин курьера
    public static final String LOGIN_COURIER_PATH = "/api/v1/courier/login";
    // удаление курьера
    public static final String DELETE_COURIER_PATH = "/api/v1/courier/";

    // создать заказ и удалить заказ
    public static final String CREATE_ORDER_PATH = "/api/v1/orders";
    public static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel/";

    // получить список заказов
    public static final String ORDER_LIST_PATH = "/api/v1/orders";
}
