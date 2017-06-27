package simulation.simulators;

import company.company.Company;
import economy.Economy;
import simulation.main.Parametrizer;
import simulation.simulators.runners.CompanySimulatorRunner;
import simulation.simulators.runners.EconomySimulatorRunner;
import simulation.simulators.runners.TelemetryDataSimulatorRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Simulates the supply chain control for a company.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class SupplyChainControlSimulator {

    private  Company company;
    private Economy economy;
    private Parametrizer parametrizer;

    private  EconomySimulatorRunner economySimulator;
    private  CompanySimulatorRunner companySimulator;
    private TelemetryDataSimulatorRunner telemetrySimulator;

    private  final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());

    public SupplyChainControlSimulator(Company company, Economy economy, Parametrizer parametrizer) {
        this.company = company;
        this.economy = economy;
        this.parametrizer = parametrizer;

        this.economySimulator = new EconomySimulatorRunner(economy);
        this.companySimulator = new CompanySimulatorRunner(company,economy);
        this.telemetrySimulator = new TelemetryDataSimulatorRunner(company);
    }

    /**
     * Starts simulations. If showMetrics is true, also starts threads that will display data.
     */
    public void start(boolean showMetrics) {

        // Number of threads to handle for the executor
        final int threadsNbr = showMetrics ? 5 : 3;

        try {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            Logger.getGlobal().info(Long.toString(parametrizer.getDelayInMicroSeconds()));

            executor.scheduleWithFixedDelay(economySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);
            executor.scheduleWithFixedDelay(companySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);
            executor.scheduleWithFixedDelay(telemetrySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);


            if (showMetrics) {
                displayEconomicalData(executor);
                displayCompanyData(executor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Display information on economics (growth, demand, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    private void displayEconomicalData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economy.getGrowth()+", Demand: "+economy.getDemand()
                +", Sector concurrency: "+economy.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
    }

    /**
     * Display information on company (products, orders, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    private void displayCompanyData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Products: "+Integer.toString(company.getProducts().size())+", Types: "+
                Integer.toString(company.getProductTypes().size())+
                ", Orders: "+Integer.toString(company.getOrders().size())+", Deliveries: "+Integer.toString(company.getDeliveries().size())
                +", Transportation: "+Integer.toString(company.getAllTransportation().size())+
                ", Customers:"+Integer.toString(company.getCustomers().size())),0,5,TimeUnit.SECONDS);
    }
}
