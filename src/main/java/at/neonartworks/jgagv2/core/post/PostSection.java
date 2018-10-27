package at.neonartworks.jgagv2.core.post;

/**
 * 
 * PostSection. Helper-Enum. 9GAG uses int ids to distinguish between sections.
 *
 * 
 * @author Florian Wagner
 *
 */
public enum PostSection
{

	FUNNY("Funny", 1), ANIMALS("Animals", 3), WTF("WTF", 4), GAMING("Gaming", 5), AWESOME("Awesome", 7),
	FOOD_DRINKS("Food & Drinks", 8), TRAVEL("Travel", 9), GIF("GIF", 10), COSPLAY("Cosplay", 11),
	NFK_NOTFORKIDS("NFK - Not For Kids", 12), TIMELY("Timely", 14), GIRL("Girl", 16), COMIC("Comic", 17),
	SPOOKTOBER("Spooktober", 19), GUY("Guy", 20), ASK9GAG("Ask 9GAG", 31), ANIME_MANGA("Anime & Manga", 32),
	K_POP("K-Pop", 34), SPORT("Sport ", 35), SCHOOL("School", 36), DARKHUMOR("Dark Humor", 37),
	COUNTRYBALLS("Countryballs", 38), HORROR("Horror", 40), DIY_CRAFTS("DIY & Crafts", 41), SCI_TECH("Sci-Tech", 42),
	POLITICS("Politics ", 43), RELATIONSHIP("Relationship", 44), SAVAGE("Savage", 45), GIRLYTHINGS("Girly Things ", 47),
	SUPERHERO("Superhero", 49), VIDEO("Video", 57), OVERWATCH("Overwatch", 60), CAR("Car", 62), MUSIC("Music", 63),
	PCMASTERRACE("PC Master Race", 64), WALLPAPER("Wallpaper", 65), HISTORY("History", 66), MOVIE_TV("Movie & TV", 70),
	SURREALMEMES("Surreal Memes", 71), CLASSICALARTMEMES("Classical Art Memes", 72), PICOFTHEDAY("Pic Of The Day", 73),
	HOMEDESIGN("Home Design", 74), ROASTME("Roast Me", 75), BASKETBALL("Basketball", 76), FOOTBALL("Football", 77),
	PUBG("PUBG", 78), FORTNITE("Fortnite", 79), WARHAMMER("Warhammer", 80), LEAGUEOFLEGENDS("League of Legends", 81),
	POKÉMON("Pokémon", 82), LEGO("LEGO", 83), DRAWING_ILLUSTRATION("Drawing & Illustration", 84),
	FANART("Fan Art ", 85);

	private String name;
	private int id;

	PostSection(String name, int id)
	{
		this.name = name;
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

}
