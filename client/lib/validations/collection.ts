import * as z from "zod";

export const collectionSchema = z.object({
  elements: z.array(
    z.object({
      elements: z.array(
        z.object({
          elements: z.array(
            z.object({
              elements: z.array(
                z.object({
                  elements: z.array(
                    z.object({
                      id: z.number(),
                      publisher: z.string(),
                      seriesTitle: z.string(),
                      volumeNumber: z.string(),
                      issueNumber: z.string(),
                      publicationDate: z.string(),
                      storyTitle: z.string(),
                      description: z.string().nullable(),
                      creators: z.array(
                        z.object({
                          name: z.string(),
                        }),
                      ),
                      principleCharacters: z.array(
                        z.object({
                          name: z.string(),
                        }),
                      ),
                      value: z.number(),
                      grade: z.number(),
                      slabbed: z.boolean(),
                    }),
                  ),
                }),
              ),
              value: z.number(),
              issueNumber: z.string(),
              volumeNumber: z.string(),
              seriesTitle: z.string(),
              publisher: z.string(),
              storyTitle: z.string(),
              numberOfIssues: z.number(),
            }),
          ),
          value: z.number(),
          issueNumber: z.string(),
          volumeNumber: z.string(),
          seriesTitle: z.string(),
          publisher: z.string(),
          storyTitle: z.string(),
          numberOfIssues: z.number(),
        }),
      ),
      value: z.number(),
      issueNumber: z.string(),
      volumeNumber: z.string(),
      seriesTitle: z.string(),
      publisher: z.string(),
      storyTitle: z.string(),
      numberOfIssues: z.number(),
    }),
  ),
  value: z.number(),
  issueNumber: z.string(),
  volumeNumber: z.string(),
  seriesTitle: z.string(),
  publisher: z.string(),
  storyTitle: z.string(),
  numberOfIssues: z.number(),
});
