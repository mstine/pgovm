package com.mattstine.polyglotosgi.vendingmachine.clojure.internal;

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine;

/**
 * Extension of the default OSGi bundle activator
 */
public final class ExampleActivator
    implements BundleActivator
{
    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start( BundleContext bc )
        throws Exception
    {
        System.out.println( "STARTING com.mattstine.polyglotosgi.vendingmachine.clojure" );

        Dictionary props = new Properties();
        // add specific service properties here...

        System.out.println( "REGISTER com.mattstine.polyglotosgi.vendingmachine.clojure.internal.clojure-vending-machine" );

        // Register our example service implementation in the OSGi service registry
        bc.registerService( VendingMachine.class.getName(), new com.mattstine.polyglotosgi.vendingmachine.clojure.internal.vendingmachine_clojure_impl(), props );
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop( BundleContext bc )
        throws Exception
    {
        System.out.println( "STOPPING com.mattstine.polyglotosgi.vendingmachine.clojure.internal.clojure-vending-machine" );

        // no need to unregister our service - the OSGi framework handles it for us
    }
}

