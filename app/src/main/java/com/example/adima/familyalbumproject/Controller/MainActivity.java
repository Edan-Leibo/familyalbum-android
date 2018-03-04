package com.example.adima.familyalbumproject.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Controller.Album.AlbumFragment;
import com.example.adima.familyalbumproject.Controller.Album.CreateAlbumFragment;
import com.example.adima.familyalbumproject.Controller.Albums.AlbumsFragment;
import com.example.adima.familyalbumproject.Controller.Login.LoginFragment;
import com.example.adima.familyalbumproject.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnFragmentInteractionListener {

    private List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
   
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLoginFragment();
    }






    public void showLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    public void showAlbumsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AlbumsFragment.newInstance(albums))
                .commit();
    }

    public void showCreateAlbumFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new CreateAlbumFragment())
                .commit();
    }


    public void showAlbumFragment() {
        showAlbumFragment(albums.get(0));
    }

    public void showAlbumFragment(Album album) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AlbumFragment.newInstance(album))
                .commit();
    }

/*
    public void showCommentsFragment(Album album) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, CommentsFragment.newInstance(album))
                .commit();
    }
*/


    @Override
    public void onItemSelected(Album album) {

    }
}