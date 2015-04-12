package it.cosenonjaviste.introtoretrofitrxjava.loaders;

import android.content.Context;
import android.widget.ArrayAdapter;

import it.cosenonjaviste.introtoretrofitrxjava.model.BadgeResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.TagResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.User;
import it.cosenonjaviste.introtoretrofitrxjava.model.UserResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.UserStats;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Loader05RxFlatMap extends DataLoader {

    public void loadItems(ArrayAdapter<Object> adapter, Context context) {
        service.getTopUsers()
                .flatMapIterable(UserResponse::getItems)
                .limit(5)
                .flatMap(this::loadRepoStats)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::addAll, t -> showError(context));
    }

    private Observable<UserStats> loadRepoStats(User user) {
        return service.getTags(user.getId())
                .map(TagResponse::getItems)
                .flatMap(tags ->
                                service.getBadges(user.getId())
                                        .map(BadgeResponse::getItems)
                                        .map(badges -> new UserStats(user, tags, badges))
                );
    }
}