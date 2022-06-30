/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 14:13
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.common;

/**
 * 自定义业务异常
 * @author Tecoy
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
