package com.mec.application.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mec.application.beans.PluginService.ViewService;

public interface PluginInfo {

	
	
	/**
	 * Plugin entry class should have at least one of its method annotated with this annotation,
	 * whose arguments may be empty, or one argument with type of <code>{@link PluginContext}</code>.
	 * e.g.: The entry method should have one of these two forms: 
	 * <ul>
	 * <li>{ReturnType} methodName()</li>
	 * <li>{ReturnType} methodName(PluginContext pc}</li>
	 * <ul>
	 * @author MEC
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)	//<- needed, else its default value is RetentionPolicy.CLASS
	@Documented
	@Target({ElementType.METHOD})
	public static @interface PluginStart{
		
	}
	

	
	/**
	 * Provides plugin context for the plugin entry method
	 * @author MEC
	 *
	 */
	interface PluginContext{
		ViewService getViewService();
	}
	
}
