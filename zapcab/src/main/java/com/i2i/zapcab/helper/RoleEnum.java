package com.i2i.zapcab.helper;

/**
 * <p>
 *     The user can have many roles. This association is achieved by this enum calss
 * </p>
 * <p>
 *     NOTE: According to the requirements of the project in JUL 2024, the user will have following roles,
 * </p>
 * <ol>
 *     <li>ADMIN</li>
 *     <li>CUSTOMER</li>
 *     <li>DRIVER</li>
 * </ol>
 */
public enum RoleEnum {
    driver,
    customer,
    admin;
}
