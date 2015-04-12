package it.cosenonjaviste.introtoretrofitrxjava;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.introtoretrofitrxjava.model.BadgeResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.TagResponse;
import it.cosenonjaviste.introtoretrofitrxjava.model.User;
import it.cosenonjaviste.introtoretrofitrxjava.model.UserStats;

public class Activity02MultipleCalls extends BaseActivity {

    @Override protected void loadItems() {

        new AsyncTask<Void, Void, List<UserStats>>() {
            @Override protected List<UserStats> doInBackground(Void... params) {
                try {
                    return loadItemsSync();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override protected void onPostExecute(List<UserStats> users) {
                if (users != null) {
                    adapter.addAll(users);
                } else {
                    showError();
                }
            }
        }.execute();
    }

    private List<UserStats> loadItemsSync() {
        List<User> users = service.getTopUsersSync().getItems();
        if (users.size() > 5) {
            users = users.subList(0, 5);
        }
        List<UserStats> statsList = new ArrayList<>();
        for (User user : users) {
            TagResponse tags = service.getTagsSync(user.getId());
            BadgeResponse badges = service.getBadgesSync(user.getId());

            statsList.add(new UserStats(user, tags.getItems(), badges.getItems()));
        }
        return statsList;
    }
}