import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BackwardSeekTest.class,
        CategoryDeselectTest.class,
        ClearAllTest.class,
        EditNotesTest.class,
        ForwardSeekTest.class,
        HighlightNotesTest.class,
        HomeButtonTest.class,
        LoadMenuTest.class,
        MultiCategoryTest.class,
        NotesRefreshTest.class,
        NotesValidationTest.class,
        NoteTakingTest.class,
        PauseButtonTest.class,
        PlayButtonTest.class,
        SearchKeyWordTest.class,
        ShareNotesTest.class,
        SingleCategoryTest.class,
        TimestampVerificationTest.class,
        VideoCountTest.class,
        VideoThumbnailAndTitleTest.class,
})

public class JunitTestSuite {
}