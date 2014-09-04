package tst.rabbitmqpoc.consumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;

/**
 * Spring beans proxy. Used to proxy the lifecycle calls to underlying beans.
 * 
 * @param <T>
 */
public class BeansProxy<T extends SmartLifecycle & BeanNameAware & DisposableBean> implements SmartLifecycle, BeanNameAware, DisposableBean {

	private final List<T> beans = new ArrayList<T>();
	private String beanName;

	public void addBean(T bean) {

		beans.add(bean);
	}

	@Override
	public void start() {

		for (T bean : beans) {
			bean.start();
		}
	}

	@Override
	public void stop() {

		for (T bean : beans) {
			bean.stop();
		}
	}

	@Override
	public boolean isRunning() {

		for (T bean : beans) {
			if (bean.isRunning()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPhase() {

		return (beans.isEmpty() ? 0 : beans.get(0).getPhase());
	}

	@Override
	public boolean isAutoStartup() {

		for (T bean : beans) {
			if (bean.isAutoStartup()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void stop(Runnable callback) {

		for (T bean : beans) {
			bean.stop(callback);
		}
	}

	@Override
	public void destroy() throws Exception {

		for (T bean : beans) {
			bean.destroy();
		}
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	protected final String getBeanName() {
		return this.beanName;
	}
}
