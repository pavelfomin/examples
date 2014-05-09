package grails.test

class TestController {

    def index() { 
		render getClass().getName() + ".index @ "+ System.currentTimeMillis()
	}
}
