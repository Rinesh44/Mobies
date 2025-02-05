package com.example.mobies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mobies.ui.theme.MobiesTheme
import com.example.mobies.ui.theme.screens.favorites.Favorites
import com.example.mobies.ui.theme.screens.favorites.FavoritesViewmodel
import com.example.mobies.ui.theme.screens.home.Home
import com.example.mobies.ui.theme.screens.home.HomeViewmodel
import com.example.mobies.ui.theme.screens.movie_detail.MovieDetailScreen
import com.example.mobies.ui.theme.screens.movie_detail.MovieDetailViewModel
import com.example.mobies.ui.theme.screens.search.Search
import com.example.mobies.ui.theme.screens.search.SearchViewmodel
import com.example.mobies.ui.theme.util.FavoritesScreen
import com.example.mobies.ui.theme.util.HomeScreen
import com.example.mobies.ui.theme.util.MovieDetailScreen
import com.example.mobies.ui.theme.util.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

data class NavigationBarItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobiesTheme {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                val navController = rememberNavController()

                var isSelectedNavigationItem by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navItemList = getNavItemsList()
                            navItemList.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    label = {
                                        Text(text = item.title)
                                    },
                                    selected = isSelectedNavigationItem == index,
                                    onClick = {
                                        isSelectedNavigationItem = index
                                        when (isSelectedNavigationItem) {
                                            0 -> {
                                                navController.navigate(HomeScreen)
                                            }

                                            1 -> {
                                                navController.navigate(SearchScreen)
                                            }

                                            2 -> {
                                                navController.navigate(FavoritesScreen)
                                            }

                                        }
                                    },
                                    icon = {
                                        BadgedBox(badge = {
                                            if (item.hasNews) {
                                                Badge()
                                            } else if (item.badgeCount != null) {
                                                Badge() {
                                                    Text(text = item.badgeCount.toString())
                                                }
                                            }
                                        }) {
                                            Icon(
                                                imageVector = if (index == isSelectedNavigationItem)
                                                    item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { HomeTopBar(scrollBehavior = scrollBehavior) }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = HomeScreen
                    ) {
                        composable<HomeScreen> {
                            val viewModel = hiltViewModel<HomeViewmodel>()
                            Home(
                                nowPlayingState = viewModel.nowPlayingState,
                                upcomingState = viewModel.upcomingMoviesState,
                                topRatedMoviesState = viewModel.topRatedMoviesState,
                                popularState = viewModel.popularMoviesState,
                                retryAction = {},
                                contentPadding = paddingValues,
                            ) {
                                navController.navigate(
                                    MovieDetailScreen(
                                        it?.id!!,
                                        it?.title,
                                        it?.posterPath,
                                        it?.voteAverage.toString(),
                                        it?.overview,
                                        it?.releaseDate
                                    )
                                )
                            }
                        }

                        composable<SearchScreen> {
                            val viewmodel = hiltViewModel<SearchViewmodel>()
                            Search(
                                searchState = viewmodel.searchState,
                                contentPadding = paddingValues,
                                searchEvent = {
                                    viewmodel.searchMovies(it)
                                },
                                onclick = {
                                    navController.navigate(
                                        MovieDetailScreen(
                                            it.id!!,
                                            it.title,
                                            it.posterPath,
                                            it.voteAverage.toString(),
                                            it.overview,
                                            it.releaseDate,
                                        )
                                    )
                                }
                            )
                        }

                        composable<FavoritesScreen> {
                            val viewmodel = hiltViewModel<FavoritesViewmodel>()
                            Favorites(viewmodel, paddingValues) {
                                viewmodel.removeFromFavorites(it)
                            }
                        }

                        composable<MovieDetailScreen> {
                            val viewmodel = hiltViewModel<MovieDetailViewModel>()
                            val args = it.toRoute<MovieDetailScreen>()
                            MovieDetailScreen(
                                movieId = args.id,
                                movieTitle = args.title ?: "",
                                moviePoster = args.poster ?: "",
                                movieRating = args.rating ?: "",
                                movieDesc = args.desc ?: "",
                                movieReleaseDate = args.releaseDate ?: "",
                                movieDetailViewModel = viewmodel,
                                contentPadding = paddingValues
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getNavItemsList(): List<NavigationBarItems> {
        return listOf(
            NavigationBarItems(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = true
            ),
            NavigationBarItems(
                title = "Search",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
            ),

            NavigationBarItems(
                title = "Favorite",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                hasNews = false,
                badgeCount = 5
            )
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            modifier = modifier
        )
    }
}

