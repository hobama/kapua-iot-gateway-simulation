/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

package simulation.simulators.company;

import company.company.Company;
import company.delivery.Delivery;
import company.order.Order;
import economy.Economy;
import simulation.util.ProbabilityUtils;

/**
 * Simulates everything related to deliveries (except telemetry data)
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DeliverySimulator extends AbstractCompanyComponentSimulator {

    public DeliverySimulator(Company company, Economy economy) {
        super(company, economy);
    }

    @Override
    public void run() {
        simulateNewDeliveries();
    }

    /**
     * Simulates new deliveries.
     * @since 1.0
     */
    private void simulateNewDeliveries() {
        //TODO assign multiple orders to one delivery (check total weight against transportation capacity)
        Order[] orders = company.getOrders().toArray(new Order[company.getOrders().size()]);
        for (Order order : orders)
            if (probabilityUtils.event(3, ProbabilityUtils.TimeUnit.DAY))
                company.getAvailableTransportation().ifPresent(transportation ->
                    company.newDelivery(new Delivery(order, transportation, company.getHeadquarters(), order.getBuyer().getAddress()))
                );
    }
}
