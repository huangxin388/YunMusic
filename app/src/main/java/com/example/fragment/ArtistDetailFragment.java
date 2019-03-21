package com.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ArtistDetailFragment extends Fragment {
    public static ArtistDetailFragment newInstance(long id) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putLong("artist_id", id);
        fragment.setArguments(args);
        return fragment;
    }
}
