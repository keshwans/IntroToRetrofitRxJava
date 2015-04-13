package it.cosenonjaviste.introtoretrofitrxjava.loaders;

import java.util.List;

import it.cosenonjaviste.introtoretrofitrxjava.StackOverflowService;
import it.cosenonjaviste.introtoretrofitrxjava.model.BadgeResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.TagResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.User;
import it.cosenonjaviste.introtoretrofitrxjava.model.UserResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.UserStats;
import rx.Observable;

public class Loader07RxZip extends RxDataLoader<UserStats> {

    public Loader07RxZip(StackOverflowService service) {
        super(service);
    }

    public Observable<List<UserStats>> loadItems() {
        return service.getTopUsers()
                .flatMapIterable(UserResponse::getItems)
                .limit(5)
                .concatMap(this::loadRepoStats)
                .toList();
    }

    private Observable<UserStats> loadRepoStats(User user) {
        return Observable.zip(
                service.getTags(user.getId()).map(TagResponse::getItems),
                service.getBadges(user.getId()).map(BadgeResponse::getItems),
                (tags, badges) -> new UserStats(user, tags, badges)
        );
    }
}