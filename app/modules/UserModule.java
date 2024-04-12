package modules;


import com.google.inject.AbstractModule;
import respositories.UserRepository;
import respositories.UserRepositoryImpl;

public class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
    }
}
